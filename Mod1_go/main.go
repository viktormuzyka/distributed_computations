package main

import (
	"fmt"
	"sync"
	"time"
)

func doWork(busCount, busLimit int) {
	waitGr := &sync.WaitGroup{}
	regulator := make(chan struct{}, busLimit)
	for i := 0; i < busCount; i++ {
		waitGr.Add(1)
		go tryToStayAtBusStop(regulator, waitGr, i)
	}
	waitGr.Wait()
}

func tryToStayAtBusStop(regulator chan struct{}, wg *sync.WaitGroup, busNumber int) {
	regulator <- struct{}{}
	defer wg.Done()
	fmt.Printf("Bus %v come on station\n", busNumber)
	time.Sleep(time.Millisecond * 3000)
	fmt.Printf("Bus %v leave from station\n", busNumber)
	<-regulator
}

func main() {
	doWork(5, 2)
}
