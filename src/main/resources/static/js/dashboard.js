const graficos = {};

function parametros() {
    const params = new URLSearchParams();
    for (const [campo, valor] of new FormData(document.getElementById('filtros'))) {
        if (valor) {
            params.append(campo, valor);
        }
    }
    return params.toString();
}

async function buscar(endpoint) {
    const resposta = await fetch(`${endpoint}?${parametros()}`);
    if (!resposta.ok) {
        throw new Error(`erro ${resposta.status} em ${endpoint}`);
    }
    return resposta.json();
}

function formatarData(data) {
    return data ? data.split('-').reverse().join('/') : '-';
}

function mostrarMensagem(texto, tipo) {
    const mensagem = document.getElementById('mensagem');
    mensagem.textContent = texto;
    mensagem.className = texto ? `alert alert-${tipo} mt-3` : 'alert d-none mt-3';
}

function preencherIndicadores(indicadores) {
    for (const [campo, valor] of Object.entries(indicadores)) {
        document.getElementById(campo).textContent = valor;
    }
}

function desenharGrafico(id, tipo, dados, legenda) {
    if (graficos[id]) {
        graficos[id].destroy();
    }
    graficos[id] = new Chart(document.getElementById(id), {
        type: tipo,
        data: {
            labels: Object.keys(dados).map(rotulo => tipo === 'bar' ? formatarData(rotulo) : rotulo),
            datasets: [{ label: legenda, data: Object.values(dados), maxBarThickness: 60 }]
        },
        options: {
            plugins: { legend: { display: tipo !== 'bar' } },
            scales: tipo === 'bar' ? { y: { beginAtZero: true, ticks: { precision: 0 } } } : {}
        }
    });
}

function criarLinha(valores) {
    const linha = document.createElement('tr');
    for (const valor of valores) {
        const celula = document.createElement('td');
        celula.textContent = valor ?? '-';
        linha.appendChild(celula);
    }
    return linha;
}

function preencherRanking(tipos) {
    const corpo = document.getElementById('rankingTipos');
    corpo.replaceChildren(...Object.entries(tipos).map(([tipo, total]) => criarLinha([tipo, total])));
}

function preencherTabela(chamados) {
    const corpo = document.getElementById('tabelaChamados');
    corpo.replaceChildren(...chamados.map(c => criarLinha([
        c.descricao, c.status, c.tipo, c.tecnico, c.usuario,
        formatarData(c.dataAbertura), formatarData(c.dataConclusao), c.observacaoTecnica
    ])));
}

async function carregarDashboard() {
    mostrarMensagem('');
    try {
        const [indicadores, periodo, status, tipos, chamados] = await Promise.all([
            buscar('/api/dashboard/indicadores'),
            buscar('/api/dashboard/atendidos-por-periodo'),
            buscar('/api/dashboard/status'),
            buscar('/api/dashboard/tipos'),
            buscar('/api/chamados')
        ]);
        preencherIndicadores(indicadores);
        desenharGrafico('graficoPeriodo', 'bar', periodo, 'Chamados concluídos');
        desenharGrafico('graficoStatus', 'doughnut', status, 'Chamados');
        preencherRanking(tipos);
        preencherTabela(chamados);
        document.getElementById('ultimaAtualizacao').textContent = new Date().toLocaleTimeString('pt-BR');
        if (chamados.length === 0) {
            mostrarMensagem('Nenhum chamado encontrado para os filtros informados.', 'warning');
        }
    } catch (erro) {
        mostrarMensagem(`Não foi possível carregar os dados (${erro.message}). Tente atualizar a página.`, 'danger');
    }
}

document.getElementById('filtros').addEventListener('submit', evento => {
    evento.preventDefault();
    carregarDashboard();
});

document.getElementById('limpar').addEventListener('click', () => {
    document.getElementById('filtros').reset();
    carregarDashboard();
});

document.getElementById('atualizar').addEventListener('click', carregarDashboard);

carregarDashboard();
setInterval(carregarDashboard, 60000);
