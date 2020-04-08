job('MNTLAB-meremin-main-build-job') {
    description 'Main job'
    parameters {
    	choiceParam('BRANCH_NAME', ['meremin', 'master'], 'Branch name')
        activeChoiceParam('BUILD_TRIGGER') {
            choiceType('CHECKBOX')
            groovyScript {
                script('''return ["MNTLAB-meremin-child1-build-job", 
                       "MNTLAB-meremin-child2-build-job", 
                       "MNTLAB-meremin-child3-build-job", 
                       "MNTLAB-meremin-child4-build-job"]
						''')
                fallbackScript('"fallback choice"')
            }
        }
    }
    steps {
        downstreamParameterized {
            trigger('$BUILD_TRIGGER') {
				block {
					buildStepFailure("FAILURE")
					unstable("UNSTABLE")
					failure("FAILURE")
				}
                parameters {
                    predefinedProp('BRANCH_NAME', '$BRANCH_NAME')
                }
            }
        }
    }
}


