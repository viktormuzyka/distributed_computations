package main

import (
	"fmt"
	"math/rand"
	"time"
)

const Duration = 1000

type Parts struct {
	itemsArray [3]bool
}

func (c *Parts) printParts() string {
	partsName := [3]string{"tobacco", "paper", "match"}
	result := ""
	isNeedTimer := true

	for i := 0; i < len(c.itemsArray); i++ {
		if !c.itemsArray[i] {
			continue
		}

		result += partsName[i]
		if isNeedTimer {
			result += ", "
			isNeedTimer = false
		}
	}

	return result
}

func genParts() (*Parts, int) {
	array := [3]bool{true, true, true}
	idx := rand.Int() % len(array)
	array[idx] = false
	return &Parts{array}, idx
}

func mediator(pingChanArray []chan *Parts, semaphore chan bool) {
	for {
		semaphore <- true
		toPush, idx := genParts()
		fmt.Println("Mediator generated : ", toPush.printParts())
		pingChanArray[idx] <- toPush
	}
}

type Smoker struct {
	name         string
	mediatorChan chan *Parts
}

func (smoker *Smoker) smoke(semaphore chan bool) {
	for {
		<-smoker.mediatorChan
		fmt.Printf("%s started smoking...\n", smoker.name)
		time.Sleep(Duration * time.Millisecond)
		fmt.Printf("%s finished smoking...\n", smoker.name)
		<-semaphore
	}
}

func main() {
	manWithTobacco := Smoker{"Tobacco owner", make(chan *Parts)}
	manWithPaper := Smoker{"Paper owner", make(chan *Parts)}
	manWithMatch := Smoker{"Match owner", make(chan *Parts)}

	semaphore := make(chan bool, 1)
	pingChanArray := []chan *Parts{manWithTobacco.mediatorChan, manWithPaper.mediatorChan, manWithMatch.mediatorChan}

	go mediator(pingChanArray, semaphore)
	go manWithTobacco.smoke(semaphore)
	go manWithPaper.smoke(semaphore)
	go manWithMatch.smoke(semaphore)

	for {
		time.Sleep(1000 * time.Millisecond)
	}
}
