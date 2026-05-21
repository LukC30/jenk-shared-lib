def call(Map config = [:]) {
    // 1. Recebendo os parâmetros do contrato
    def linguagem = config.linguagem ?: "python"
    def nome = config.nomeProjeto ?: "projeto-desconhecido"

    // 2. A Esteira Declarativa
    pipeline {
        agent any
        
        stages {
            stage('Setup Inicial') {
                steps {
                    echo "======================================================="
                    echo " Iniciando Esteira Padronizada - QAOps                 "
                    echo " Projeto: ${nome}                                      "
                    echo " Stack: ${linguagem.toUpperCase()}                     "
                    echo "======================================================="
                    
                    // Mockando um arquivo para o Linter não quebrar por falta de código
                    sh 'echo "import os\n\ndef main():\n    pass" > main.py'
                }
            }
            
            stage('Validação de Código (Fail-Fast)') {
                steps {
                    script {
                        if (linguagem == 'python') {
                            echo "Inspecionando Python com Ruff..."
                            sh "docker run --rm -v \${WORKSPACE}:/app -w /app ghcr.io/astral-sh/ruff:latest check ."
                        } 
                        else if (linguagem == 'go') {
                            echo "Inspecionando Go com golangci-lint..."
                            sh "docker run --rm -v \${WORKSPACE}:/app -w /app golangci-lint/golangci-lint:v1.55 golangci-lint run -v"
                        }
                        else {
                            error "Falha na esteira: Linguagem ${linguagem} não homologada."
                        }
                    }
                }
            }
        }
        
        // 3. Ações de Pós-Execução (Limpeza)
        post {
            always {
                echo "Limpando o workspace para não acumular lixo no Ubuntu Server..."
                cleanWs()
            }
        }
    }
}