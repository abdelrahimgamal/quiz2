package myapp.aishari.aishari_oblig2.data.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root


@Root(name = "party")
data class VotesXml (
    @field:Element(name = "id")
    @param:Element(name = "id")
    var id: Int,
    @field:Element(name = "votes")
    @param:Element(name = "votes")
    var votes: Int,
)

@Root(name = "districtThree")
data class DistrictThree @JvmOverloads constructor(
    @field:ElementList(name = "party", inline = true)
    @param:ElementList(name = "party", inline = true)
    var parties: List<VotesXml>? = null
)

