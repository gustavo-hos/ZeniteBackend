db = db.getSiblingDB('zenite');

db.createUser({
    user: process.env.MONGO_APP_USER || 'zenite',
    pwd:  process.env.MONGO_APP_PASS || 'zenite',
    roles: [{ role: 'readWrite', db: 'zenite' }]
});


db.questionnaires.drop();

function q(text, options) {
    return { text: text, type: "single-choice", options: options };
}

const OPT_EMOJI = ["Triste","Alegre","Cansado","Ansioso","Medo","Raiva"];
const OPT_HUMOR = ["Motivado","Cansado","Preocupado","Estressado","Animado","Satisfeito"];
const OPT_CARGA = ["Muito Leve","Leve","Média","Alta","Muito Alta"];
const OPT_FREQ_5 = ["Nunca","Raramente","Às vezes","Frequentemente","Sempre"];
const OPT_FREQ_5_BOOL = ["Não","Raramente","Às vezes","Frequentemente","Sempre"];
const OPT_LIKERT_1_5 = ["1","2","3","4","5"];

const fatoresCargaTrabalho = {
    title: "Fatores de Carga de Trabalho",
    description: "Percepção de carga e impacto na vida pessoal",
    questions: [
        q("Como você avalia sua carga de trabalho?", OPT_CARGA),
        q("Sua carga de trabalho afeta sua qualidade de vida?", OPT_FREQ_5_BOOL),
        q("Você trabalha além do seu horário regular?", OPT_FREQ_5_BOOL)
    ]
};

const sinaisDeAlerta = {
    title: "Sinais de Alerta",
    description: "Indicadores pessoais de estresse/saúde mental",
    questions: [
        q("Você tem apresentado sintomas como insônia, irritabilidade ou cansaço extremo?", OPT_FREQ_5),
        q("Você sente que sua saúde mental prejudica sua produtividade no trabalho?", OPT_FREQ_5)
    ]
};

const diagnosticoClimaRelacionamento = {
    title: "Diagnóstico de Clima - Relacionamento",
    description: "Relações no ambiente de trabalho",
    questions: [
        q("Como está o seu relacionamento com seu chefe numa escala de 1 a 5? (Sendo 01 - ruim e 05 - Ótimo)", OPT_LIKERT_1_5),
        q("Como está o seu relacionamento com seus colegas de trabalho numa escala de 1 a 5? (Sendo 01 - ruim e 05 - Ótimo)", OPT_LIKERT_1_5),
        q("Sinto que sou tratado(a) com respeito pelos meus colegas de trabalho.", OPT_LIKERT_1_5),
        q("Consigo me relacionar de forma saudável e colaborativa com minha equipe.", OPT_LIKERT_1_5),
        q("Tenho liberdade para expressar minhas opiniões sem medo de retaliações.", OPT_LIKERT_1_5),
        q("Me sinto acolhido(a) a parte do time onde trabalho.", OPT_LIKERT_1_5),
        q("Sinto que existe espírito de cooperação entre os colaboradores.", OPT_LIKERT_1_5)
    ]
};

const comunicacao = {
    title: "Comunicação",
    description: "Clareza, fluxo e abertura da comunicação",
    questions: [
        q("Recebo orientações claras e objetivas sobre minhas atividades e responsabilidades.", OPT_LIKERT_1_5),
        q("Sinto que posso me comunicar abertamente com minha liderança.", OPT_LIKERT_1_5),
        q("As informações importantes circulam de forma eficiente dentro da empresa.", OPT_LIKERT_1_5),
        q("Tenho clareza sobre as metas e os resultados esperados de mim.", OPT_LIKERT_1_5)
    ]
};

const relacaoComLideranca = {
    title: "Relação com a Liderança",
    description: "Confiança, apoio e reconhecimento da liderança",
    questions: [
        q("Minha liderança demonstra interesse pelo meu bem-estar no trabalho", OPT_LIKERT_1_5),
        q("Minha liderança está disponível para me ouvir quando necessário.", OPT_LIKERT_1_5),
        q("Me sinto confortável para reportar problemas ou dificuldades ao meu líder", OPT_LIKERT_1_5),
        q("Minha liderança reconhece minhas entregas e esforços", OPT_LIKERT_1_5),
        q("Existe confiança e transparência na relação com minha liderança", OPT_LIKERT_1_5)
    ]
};

db.questionnaires.insertMany([
    fatoresCargaTrabalho,
    sinaisDeAlerta,
    diagnosticoClimaRelacionamento,
    comunicacao,
    relacaoComLideranca
]);

print("[init-mongo] Questionários inseridos com sucesso.");
