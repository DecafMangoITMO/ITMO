package main

import (
	"fmt"
	"math/big"
)

func solution(number *big.Int) *big.Int {
	sum := big.NewInt(0)
	temp := big.NewInt(0)
	base := big.NewInt(10)

	for number.Cmp(big.NewInt(0)) != 0 {
		temp.Set(number)
		sum.Add(sum, temp.Mod(temp, base))
		number.Div(number, base)
	}

	return sum
}

func main() {
	var pow int64
	_, err := fmt.Scan(&pow)

	if err != nil || pow <= 0 {
		fmt.Println("Error")
		return
	}

	number := big.NewInt(2)
	number.Exp(number, big.NewInt(pow), nil)

	fmt.Println(solution(number))
}