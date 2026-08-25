package com.example.mythos.data

import com.example.mythos.model.Comparison
import com.example.mythos.model.Deity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Backend do tema (Mitologia Comparada).
 *
 * Os dados ficam no Cloud Firestore, nas coleções "deities" e "comparisons".
 * Na primeira execução o app semeia o acervo (seedIfEmpty) para que a demonstração
 * no Firebase Console mostre os documentos do tema.
 */
class MythologyRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun seedIfEmpty() {
        val deities = db.collection("deities").get().await()
        if (deities.isEmpty) {
            LOCAL_DEITIES.forEach { deity ->
                db.collection("deities").document(deity.id).set(deity).await()
            }
        }
        val comparisons = db.collection("comparisons").get().await()
        if (comparisons.isEmpty) {
            LOCAL_COMPARISONS.forEach { comparison ->
                db.collection("comparisons").document(comparison.id).set(comparison).await()
            }
        }
    }

    suspend fun getDeities(): List<Deity> {
        val snapshot = db.collection("deities").get().await()
        val remote = snapshot.documents.mapNotNull { it.toObject(Deity::class.java) }
        return if (remote.isEmpty()) LOCAL_DEITIES else remote
    }

    suspend fun getCuriosity(firstId: String, secondId: String): String {
        val snapshot = db.collection("comparisons").get().await()
        val remote = snapshot.documents.mapNotNull { it.toObject(Comparison::class.java) }
        val list = if (remote.isEmpty()) LOCAL_COMPARISONS else remote
        val found = list.firstOrNull {
            (it.firstId == firstId && it.secondId == secondId) ||
                (it.firstId == secondId && it.secondId == firstId)
        }
        return found?.curiosity
            ?: "Culturas diferentes representaram forças da natureza em esculturas e afrescos, " +
            "criando figuras divinas com atributos muito parecidos."
    }

    companion object {
        val LOCAL_DEITIES = listOf(
            Deity(
                id = "zeus",
                name = "Zeus",
                culture = "Grega",
                period = "Século V a.C.",
                epithet = "Deus do céu e do trovão",
                description = "Zeus é o soberano do Olimpo e o deus dos céus, dos trovões e " +
                    "dos relâmpagos. Representa a justiça, a ordem e o poder divino. " +
                    "É retratado em esculturas monumentais de mármore, sentado em trono, " +
                    "com barba longa e o raio em uma das mãos.",
                power = "Trovão",
                symbol = "Águia",
                domain = "Céu e Justiça",
                artwork = "Escultura em mármore (estatuária clássica)"
            ),
            Deity(
                id = "ra",
                name = "Rá",
                culture = "Egípcia",
                period = "Século XIII a.C.",
                epithet = "Deus do sol",
                description = "Rá é o deus solar do panteão egípcio, criador e senhor do dia. " +
                    "Aparece em relevos e afrescos de tumbas com cabeça de falcão e o " +
                    "disco solar sobre a cabeça, navegando o céu em sua barca.",
                power = "Luz solar",
                symbol = "Disco solar",
                domain = "Sol e Criação",
                artwork = "Relevo e afresco funerário"
            ),
            Deity(
                id = "jupiter",
                name = "Júpiter",
                culture = "Romana",
                period = "Século I d.C.",
                epithet = "Rei dos deuses romanos",
                description = "Júpiter é a versão romana de Zeus, protetor do Estado e da lei. " +
                    "Suas esculturas imperiais o mostram com feições severas, coroa de " +
                    "louros e cetro, reforçando a autoridade política de Roma.",
                power = "Raio",
                symbol = "Cetro e águia",
                domain = "Céu e Estado",
                artwork = "Escultura imperial romana"
            ),
            Deity(
                id = "thor",
                name = "Thor",
                culture = "Nórdica",
                period = "Século X d.C.",
                epithet = "Deus do trovão nórdico",
                description = "Thor é o deus do trovão, filho de Odin, defensor de Asgard e " +
                    "dos homens. Aparece em pequenas estatuetas de bronze e em pedras " +
                    "entalhadas, sempre com o martelo Mjölnir e barba densa.",
                power = "Trovão e relâmpagos",
                symbol = "Martelo (Mjölnir)",
                domain = "Proteção e Guerra",
                artwork = "Estatueta de bronze e pedra entalhada"
            ),
            Deity(
                id = "indra",
                name = "Indra",
                culture = "Hindu",
                period = "Século VIII d.C.",
                epithet = "Senhor das tempestades",
                description = "Indra é o rei dos devas, deus da chuva, das tempestades e do " +
                    "trovão. É esculpido em templos com múltiplos braços e o raio " +
                    "Vajra, muitas vezes montado no elefante Airavata.",
                power = "Vajra (raio)",
                symbol = "Elefante Airavata",
                domain = "Chuva e Tempestade",
                artwork = "Escultura em templo e pintura devocional"
            ),
            Deity(
                id = "hercules",
                name = "Hércules",
                culture = "Grega",
                period = "Século IV a.C.",
                epithet = "Herói dos doze trabalhos",
                description = "Hércules (Héracles) é o herói semideus conhecido pela força " +
                    "sobre-humana e pelos doze trabalhos. A estatuária o representa " +
                    "musculoso, com a pele do leão de Nemeia e a clava.",
                power = "Força sobre-humana",
                symbol = "Clava e pele de leão",
                domain = "Coragem e Superação",
                artwork = "Escultura helenística"
            )
        )

        val LOCAL_COMPARISONS = listOf(
            Comparison(
                id = "zeus_thor",
                firstId = "zeus",
                secondId = "thor",
                curiosity = "Ambos são associados ao trovão e são considerados protetores de " +
                    "seus povos, mas Zeus aparece como soberano em tronos de mármore e " +
                    "Thor como guerreiro em estatuetas de bronze."
            ),
            Comparison(
                id = "zeus_jupiter",
                firstId = "zeus",
                secondId = "jupiter",
                curiosity = "Roma adotou o panteão grego: Júpiter é Zeus reinterpretado como " +
                    "símbolo do poder político do Império."
            ),
            Comparison(
                id = "thor_indra",
                firstId = "thor",
                secondId = "indra",
                curiosity = "Thor e Indra têm raiz indo-europeia comum: os dois empunham uma " +
                    "arma do trovão (Mjölnir e Vajra) contra serpentes/dragões."
            ),
            Comparison(
                id = "ra_zeus",
                firstId = "ra",
                secondId = "zeus",
                curiosity = "Rá governa o dia pelo sol e Zeus pelo céu: duas formas de " +
                    "representar a autoridade celeste em culturas distintas."
            )
        )
    }
}
