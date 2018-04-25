package io.humanteq.graphcharttest.models

data class SocChartModel(val friends: Int = 0,
                         val communities: List<CommunityModel2> = arrayListOf())