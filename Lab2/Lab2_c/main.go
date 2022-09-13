package main

import "fmt"

func geWinID(monks []int, start, end int) int {
	rez := start
	if monks[rez] < monks[end] {
		rez = end
	}
	return rez
}

func getNameOfMonastery(id int) string {
	rez := ""
	switch id % 2 {
	case 0:
		rez = "Guan-in'"
	case 1:
		rez = "Guan-yan'"
	}
	return rez
}

func startFights(powers []int, start, end int, channel chan<- int) {
	winner := start
	if end - start < 2 {
		winner = geWinID(powers, start, end)
	} else {
		mid := (start + end) / 2
		var subchannel = make(chan int, 2)
		go startFights(powers, start, mid, subchannel)
		startFights(powers, mid+1, end, subchannel)
		firstWinner := <-subchannel
		secondWinner := <-subchannel
		winner = geWinID(powers, firstWinner, secondWinner)
	}
	channel <- winner
}

func main() {
	powerInput := []int{10, 200, 40, 30, 60, 50, 100, 12, 20}
	var channel = make(chan int, 1)
	startFights(powerInput, 0, len(powerInput)-1, channel)
	var winnerID = <-channel
	fmt.Println("ID:"   , winnerID)
	fmt.Println("Power:"  , powerInput[winnerID], "Ci")
	fmt.Println("Monastery:" , getNameOfMonastery(winnerID))
}