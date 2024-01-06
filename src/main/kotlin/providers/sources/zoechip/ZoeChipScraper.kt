package providers.sources.zoechip

import common.SourceTypes
import entrypoint.utils.CommonMedia
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import providers.SourceCreator
import providers.sources.EmbedSources
import providers.sources.zoechip.ZoeChipSearch.getZoeChipSearchResults
import utils.errors.NotFoundError

class ZoeChipScraper: SourceCreator(SourceTypes.ZOECHIP) {

    override suspend fun getMovie(media: CommonMedia.MovieMedia) = withContext(Dispatchers.IO) {
        val movieId = getZoeChipMovieID(media)
        if (movieId.isNullOrEmpty()) throw NotFoundError("no search results match")

        return@withContext createZoeChipStreamData(media, movieId)
    }

    override suspend fun getShow(media: CommonMedia.ShowMedia) = withContext(Dispatchers.IO) {
        return@withContext EmbedSources(emptyList())
    }

}