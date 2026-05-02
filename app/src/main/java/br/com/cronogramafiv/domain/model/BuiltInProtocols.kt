package br.com.cronogramafiv.domain.model

/**
 * Built-in protocol templates available when the app starts.
 *
 * These definitions intentionally stay in the domain layer because they are
 * business defaults, not UI or persistence concerns.
 */
object BuiltInProtocols {
    val recipient: ReproductiveProtocol = ReproductiveProtocol(
        id = "recipient-standard",
        name = "Receptora - Protocolo padrão",
        type = ProtocolType.RECIPIENT,
        description = "Cronograma básico para sincronização de receptora e transferência de embriões.",
        isBuiltIn = true,
        steps = listOf(
            ProtocolStep(
                id = "recipient-d0-synchronization",
                title = "D0 - Sincronização",
                description = "Início do protocolo de sincronização da receptora.",
                dayOffset = 0,
                order = 0,
            ),
            ProtocolStep(
                id = "recipient-d8-implant-removal",
                title = "D8 - Retirada de implante",
                description = "Retirada do implante e manejo hormonal conforme protocolo.",
                dayOffset = 8,
                order = 0,
            ),
            ProtocolStep(
                id = "recipient-d17-embryo-transfer",
                title = "D17 - Transferência de embriões",
                description = "Transferência de embriões para receptoras aptas.",
                dayOffset = 17,
                order = 0,
            ),
        ),
    )

    val donorPive: ReproductiveProtocol = ReproductiveProtocol(
        id = "donor-pive-standard",
        name = "Doadora / PIVE - Protocolo padrão",
        type = ProtocolType.DONOR_PIVE,
        description = "Cronograma básico para OPU, FIV, CIV, feeding, transferência ou criopreservação.",
        isBuiltIn = true,
        steps = listOf(
            ProtocolStep(
                id = "donor-d9-opu",
                title = "D9 / D-1 - OPU",
                description = "Coleta de oócitos por OPU.",
                dayOffset = 9,
                order = 0,
            ),
            ProtocolStep(
                id = "donor-d10-fiv",
                title = "D10 / D0 - FIV",
                description = "Fertilização in vitro.",
                dayOffset = 10,
                order = 0,
            ),
            ProtocolStep(
                id = "donor-d11-civ",
                title = "D11 / D1 - CIV",
                description = "Cultivo in vitro inicial.",
                dayOffset = 11,
                order = 0,
            ),
            ProtocolStep(
                id = "donor-d13-feeding-50",
                title = "D13 / D3 - Feeding 50%",
                description = "Primeiro feeding do cultivo embrionário.",
                dayOffset = 13,
                order = 0,
            ),
            ProtocolStep(
                id = "donor-d15-feeding-80",
                title = "D15 / D5 - Feeding 80%",
                description = "Segundo feeding do cultivo embrionário.",
                dayOffset = 15,
                order = 0,
            ),
            ProtocolStep(
                id = "donor-d17-transfer-or-cryopreservation",
                title = "D17 / D7 - TE ou criopreservação",
                description = "Transferência de embriões ou criopreservação.",
                dayOffset = 17,
                order = 0,
            ),
        ),
    )

    val pregnancyFollowUp: ReproductiveProtocol = ReproductiveProtocol(
        id = "pregnancy-follow-up-standard",
        name = "Acompanhamento de prenhez - Protocolo padrão",
        type = ProtocolType.PREGNANCY_FOLLOW_UP,
        description = "Cronograma básico após transferência embrionária.",
        isBuiltIn = true,
        steps = listOf(
            ProtocolStep(
                id = "pregnancy-d0-embryo-transfer",
                title = "D0 - Transferência embrionária",
                description = "Data de referência da transferência embrionária.",
                dayOffset = 0,
                order = 0,
            ),
            ProtocolStep(
                id = "pregnancy-d1-transfer-report",
                title = "D1 - Relatório de TE",
                description = "Registro ou envio do relatório de transferência embrionária.",
                dayOffset = 1,
                order = 0,
            ),
            ProtocolStep(
                id = "pregnancy-d30-diagnosis",
                title = "D30 - Diagnóstico de prenhez",
                description = "Avaliação inicial de prenhez.",
                dayOffset = 30,
                order = 0,
            ),
            ProtocolStep(
                id = "pregnancy-d60-sexing",
                title = "D60 - Sexagem fetal",
                description = "Sexagem fetal quando aplicável.",
                dayOffset = 60,
                order = 0,
            ),
            ProtocolStep(
                id = "pregnancy-d280-expected-birth",
                title = "D280 - Nascimento previsto",
                description = "Data estimada de nascimento.",
                dayOffset = 280,
                order = 0,
            ),
        ),
    )

    val all: List<ReproductiveProtocol> = listOf(
        recipient,
        donorPive,
        pregnancyFollowUp,
    )
}
