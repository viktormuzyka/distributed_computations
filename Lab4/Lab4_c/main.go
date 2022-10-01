package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

const Interval = 5

type Route struct {
	destination string
	price       int
}

type Graph struct {
	routesList map[string][]*Route
	lock       sync.RWMutex
}

func (graph *Graph) init() {
	graph.routesList = make(map[string][]*Route)
}

func (graph *Graph) getRoute(from, to string) (*Route, int) {
	if graph.routesList[from] == nil {
		return nil, -1
	}

	var resultRoute *Route = nil
	var resultIdx int = -1

	for i := 0; i < len(graph.routesList[from]); i++ {
		current := graph.routesList[from][i]
		if current.destination == to {
			resultRoute = current
			resultIdx = i
			break
		}
	}

	return resultRoute, resultIdx
}

func (graph *Graph) addCity(name string) bool {
	if graph.routesList[name] != nil {
		return false
	}

	graph.routesList[name] = make([]*Route, 0)

	return true
}

func (graph *Graph) addRoute(from, to string, price int) bool {
	_, routeIdx := graph.getRoute(from, to)

	if routeIdx != -1 || graph.routesList[from] == nil || graph.routesList[to] == nil || (from == to) {
		return false
	}

	graph.routesList[from] = append(graph.routesList[from], &Route{to, price})
	graph.routesList[to] = append(graph.routesList[to], &Route{from, price})

	return true
}

func removeByIndex[T any](slice []T, i int) []T {
	return append(slice[:i], slice[i+1:]...)
}

func (graph *Graph) removeCity(city string) bool {
	if graph.routesList[city] == nil {
		return false
	}

	for currentCity, _ := range graph.routesList {
		if currentCity == city {
			continue
		}

		graph.removeRoute(currentCity, city)
	}

	delete(graph.routesList, city)

	return true
}

func (graph *Graph) removeRoute(from, to string) bool {
	_, idxInFromAdjList := graph.getRoute(from, to)

	if idxInFromAdjList == -1 {
		return false
	}

	_, idxInToAdjList := graph.getRoute(to, from)

	graph.routesList[from] = removeByIndex(graph.routesList[from], idxInFromAdjList)
	graph.routesList[to] = removeByIndex(graph.routesList[to], idxInToAdjList)

	return true
}

func (graph *Graph) editRoutePrice(from, to string, newPrice int) bool {
	routeTo, _ := graph.getRoute(from, to)

	if routeTo == nil {
		return false
	}

	routeFrom, _ := graph.getRoute(to, from)

	routeTo.price = newPrice
	routeFrom.price = newPrice

	return true
}

func (graph *Graph) print() {
	for city, list := range graph.routesList {
		fmt.Printf("From : %s\n", city)

		for i := 0; i < len(list); i++ {
			fmt.Printf("%s : %d\n", list[i].destination, list[i].price)
		}

		fmt.Println("----------------------------------------")
	}
}

func priceEditor(graph *Graph, cities []string) {
	from := cities[rand.Int()%len(cities)]
	to := cities[rand.Int()%len(cities)]

	if from == to {
		fmt.Println("PriceEditor: there isn't loop edges")
		return
	}

	route, _ := graph.getRoute(from, to)
	if route == nil {
		if graph.routesList[from] == nil || graph.routesList[to] == nil {
			fmt.Printf("PriceEditor: there isn't %s or %s in graph\n", from, to)
		} else {
			fmt.Printf("PriceEditor: there isn't route from %s to %s\n", from, to)
		}
	} else {
		oldPrice := route.price
		newPrice := rand.Intn(2000)
		graph.editRoutePrice(from, to, newPrice)
		fmt.Printf("PriceEditor: changed newPrice from %s to %s(before = %d, after = %d)\n", from, to, oldPrice, newPrice)
	}
}

func performPriceEditor(graph *Graph, cities []string) {
	for {
		graph.lock.Lock()
		time.Sleep(Interval * time.Second)
		priceEditor(graph, cities)
		graph.lock.Unlock()
	}
}

func routeEditor(graph *Graph, cities []string) {
	from := cities[rand.Int()%len(cities)]
	to := cities[rand.Int()%len(cities)]
	toRemove := rand.Intn(2)%2 == 0

	if toRemove {
		if graph.removeRoute(from, to) {
			fmt.Printf("RouteEditor: removed route from %s to %s\n", from, to)
		} else {
			fmt.Printf("RouteEditor: there isn't route from %s to %s\n", from, to)
		}
	} else {
		price := rand.Intn(2000)

		if graph.addRoute(from, to, price) {
			fmt.Printf("RouteEditor: added route from %s to %s, price: %d\n", from, to, price)
		} else {
			route, _ := graph.getRoute(from, to)

			if route != nil {
				fmt.Printf("RouteEditor: the route from %s to %s already exists\n", from, to)
			} else if graph.routesList[from] == nil || graph.routesList[to] == nil {
				fmt.Printf("RouteEditor: there isn't %s or %s in graph\n", from, to)
			}
		}
	}
}

func performRouteEditor(graph *Graph, cities []string) {
	for {
		graph.lock.Lock()
		time.Sleep(Interval * time.Second)
		routeEditor(graph, cities)
		graph.lock.Unlock()
	}
}

func cityEditor(graph *Graph, cities []string) {
	city := cities[rand.Int()%len(cities)]
	toRemove := rand.Intn(2)%2 == 0

	if toRemove {
		if graph.removeCity(city) {
			fmt.Printf("CityEditor: successfully removed %s city\n", city)
			//graph.print()
		} else {
			fmt.Printf("CityEditor: %s doesn't exist\n", city)
		}
	} else {
		if graph.addCity(city) {
			fmt.Printf("CityEditor: successfully added %s city\n", city)
		} else {
			fmt.Printf("CityEditor: %s already exists\n", city)
		}
	}
}

func performCityEditor(graph *Graph, cities []string) {
	for {
		graph.lock.Lock()
		time.Sleep(Interval * time.Second)
		cityEditor(graph, cities)
		graph.lock.Unlock()
	}
}

func routeFinder(graph *Graph, cities []string) {
	from := cities[rand.Int()%len(cities)]
	to := cities[rand.Int()%len(cities)]
	route, _ := graph.getRoute(from, to)

	if route != nil {
		fmt.Printf("RouteFinder: found route from %s to %s, price: %d\n", from, to, route.price)
	} else {
		fmt.Printf("RouteFinder: there isn't route from %s to %s\n", from, to)
	}
}

func performRouteFinder(graph *Graph, cities []string) {
	for {
		graph.lock.RLock()
		time.Sleep(Interval * time.Second)
		routeFinder(graph, cities)
		graph.lock.RUnlock()
	}
}

func main() {
	cities := []string{"Kyiv", "Odesa", "Lviv"}
	graph := Graph{}
	graph.init()

	for i := 0; i < len(cities); i++ {
		graph.addCity(cities[i])
	}

	graph.addRoute("Kyiv", "Odesa", 1)
	graph.addRoute("Kyiv", "Lviv", 2)
	graph.addRoute("Odesa", "Lviv", 3)

	go performPriceEditor(&graph, cities)
	go performRouteEditor(&graph, cities)
	go performCityEditor(&graph, cities)
	go performRouteFinder(&graph, cities)

	for {
	}
}
