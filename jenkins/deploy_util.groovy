
def deploy(input_map) {
	def currentCount = getDeploymentCount(input_map)
	applyLatestTag(input_map)

	// Sleep long enough for the rollout to register in the history.
	sleep 5

	def updatedCount = getDeploymentCount(input_map)

	if (updatedCount <= currentCount) {
		// This can happen if the image has not changed since the last deployment.
		println "The image tag ${input_map['tag']} did not trigger a new deployment. Triggering manually."
		rolloutLatest(input_map)
		updatedCount = getDeploymentCount(input_map)
	}

	if (updatedCount <= currentCount) {
		error("A new deployment was not initiated! Failing the build.")
	}

	monitorDeployment(input_map)
}

def getDeploymentCount(input_map) {
	println "Determining the number of deployments for [ ${input_map['label']} ]."
	shellText = getHistoryText(input_map)
	lines = shellText.split("\n")
	length = lines.length
	if (length >= 2) {
		// Remove heading lines
		length -= 2
	}
	println "There have been a total of [ ${length} ] deployments."
	return length
}

def getHistoryText(input_map) {
	/* Example output
		deploymentconfigs "lbs-pt"
		REVISION	STATUS		CAUSE
		1		Complete	image change
		2		Complete	image change
		3		Complete	image change
		4		Complete	manual change
	*/
	return sh(returnStdout: true, script: "oc -n ${input_map['project']} rollout history dc/${input_map['label']}")
}

def applyLatestTag(input_map) {
	println "Applying latest tag [ tag = ${input_map['tag']} ] to imagestream [ is = ${input_map['label']} ]"
	sh """
		oc tag -n ${input_map['project']} --insecure=true --reference-policy='local' ${input_map['registry']}/${input_map['image']}:${input_map['tag']} ${input_map['label']}:release
	"""
}

def rolloutLatest(input_map) {
	println "Issuing rollout command for deployment config [ ${input_map['label']} ]."
	sh """
		oc -n ${input_map['project']} rollout latest dc/${input_map['label']}
	"""
}

def monitorDeployment(input_map) {
	println "Monitoring deployment [ tag = ${input_map['tag']} ] [ label = ${input_map['label']} ]."

	secondsWaited = 0
	intervalSeconds = 5
	waitSeconds = 300

	deployed = isDeploymentComplete(input_map)
	while (secondsWaited < waitSeconds && !deployed) {
		sleep intervalSeconds
		println "The build has been waiting [ ${secondsWaited} ] seconds."
		secondsWaited += intervalSeconds
		deployed = isDeploymentComplete(input_map)
	}

	if (secondsWaited >= waitSeconds && !deployed) {
		error("The deployment failed to complete after [ ${secondsWaited} ] seconds.")
	}
}

def isDeploymentComplete(input_map) {
	shellText = getHistoryText(input_map)
	lines = shellText.split("\n")
	mostRecentDeployment = lines[lines.length - 1]
	mostRecentDeployment = mostRecentDeployment.toLowerCase()
	println "Deployment status: ${mostRecentDeployment}"

	if (mostRecentDeployment.contains("fail") || mostRecentDeployment.contains("error")) {
		error("The deployment failed!")
	}
	return mostRecentDeployment.contains("complete")
}

return this