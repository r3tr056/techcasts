package com.xdivision.techcasts.data.models.mapper

import com.xdivision.techcasts.data.models.local.Lyric
import com.xdivision.techcasts.data.models.remote.lyrics.LyricsDTO
import javax.inject.Inject

class LyricWrapper @Inject constructor() : Mapper<LyricsDTO, Lyric> {
    override fun toDomain(network: LyricDTO): Lyric = Lyric(
        artists = network.artists,
        fullTitle = network.fullTitle,
        lyrics = network.lyrics,
        audioId = "",
        title = network.title
    )

    override fun toDomainList(networks: List<LyricDTO>): List<Lyric> = networks.map {
        toDomain(it)
    }

    override fun toNetwork(domain: Lyric): LyricDTO = LyricsDTO(
        artists = domain.artists,
        fullTitle = domain.fullTitle,
        lyrics = domain.lyrics,
        audioId = "",
        title = domain.title
    )

    override fun toNetworkList(domains: List<Lyric>): List<LyricsDTO> = domains.map {
        toNetwork(it)
    }
}