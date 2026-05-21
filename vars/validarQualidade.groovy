def call() {
    echo "--- [QAOps] Iniciando Análise Estática de Código: PYTHON ---"

    // O comando 'sh' diz ao Jenkins para abrir um terminal no Linux Mint e rodar o script
    sh """
        docker run --rm \\
        -v \${WORKSPACE}:/app \\
        -w /app \\
        ghcr.io/astral-sh/ruff:latest check .
    """
}