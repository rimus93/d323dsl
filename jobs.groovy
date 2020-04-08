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

for (i in 1..4) {
   job("MNTLAB-meremin-child${i}-build-job") {
     	scm {
           git {
           		remote {
                	url('https://github.com/rimus93/d323dsl.git')
            	}
            }
       
    	}
   		parameters {
        	gitParam('BRANCH_NAME') {
            	type('BRANCH')
            }
    	}
        steps {
            shell('''chmod +x script.sh
                ./script.sh > output.txt
tar -czvf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy''')
        }
     	publishers {
			archiveArtifacts {
              pattern('${BRANCH_NAME}_dsl_script.tar.gz')
            }
        }
     
    }  
}

