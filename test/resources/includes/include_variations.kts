

println("Let's resolve includes!")

//INCLUDE rel_includes//include_1.kts
//INCLUDE ./rel_includes//include_2.kts

//INCLUDE ../includes/include_3.kts
//INCLUDE include_4.kts

include_1()
include_2()
include_3()
include_4()

println("wow, so many includes")
