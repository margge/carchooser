package com.margge.carchooser

import org.junit.Assert
import org.junit.Test

class GeneralUnitTest {

    private val map: HashMap<String, String> = hashMapOf(
            "25" to "25",
            "45" to "45",
            "75" to "75",
            "100" to "100",
            "200" to "200",
            "400" to "400",
            "600" to "600",
            "800" to "800",
            "Allegro" to "Allegro",
            "Innocenti" to "Innocenti",
            "MG TF" to "MG TF",
            "MG ZR" to "MG ZR",
            "MG ZS" to "MG ZS",
            "MG ZT" to "MG ZT",
            "MG ZT-T" to "MG ZT-T",
            "MGF" to "MGF",
            "MINI" to "MINI",
            "Maestro" to "Maestro",
            "Maxi" to "Maxi",
            "Metro" to "Metro",
            "Montego" to "Montego",
            "Rover Estate" to "Rover Estate",
            "Rover SD 1" to "Rover SD 1",
            "Streetwise" to "Streetwise",
            "Triumph Acclaim" to "Triumph Acclaim",
            "Triumph Spitfire/TR 7" to "Triumph Spitfire/TR 7")

    @Test
    fun validSearch() {
        val query = "MG"
        val dataDB = map.toList()

        val results = dataDB.filter { it.second.startsWith(query) }

        for (result in results) {
            Assert.assertTrue(result.second.startsWith(query))
        }

        Assert.assertTrue(results.size == 6)
    }

    @Test
    fun invalidSearch() {
        val query = "Co"
        val dataDB = map.toList()

        val results = dataDB.filter { it.second.startsWith(query) }

        for (result in results) {
            Assert.assertTrue(result.second.startsWith(query))
        }

        Assert.assertTrue(results.size == 0)
    }
}