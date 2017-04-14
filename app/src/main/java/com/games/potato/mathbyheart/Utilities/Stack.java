package com.games.potato.mathbyheart.Utilities;


//Simon Tran
public class Stack<E> {
	private int maxElements=1000;
	private E elements[];
	private int peak;
	
	public Stack() {
			elements = (E[ ]) new Object[maxElements];
			peak = -1;
	}
	
	public void stack(E newElement) {
		if (peak == (maxElements-1)) {
			System.out.println("com.games.potato.mathbyheart.math.Stack is full");
            //ToDo: Change to increase max elements
		} else {
			peak = peak + 1;
			elements[peak] = newElement;
		}
	}
	
	public E unstack() {
		if (peak < 0) {
			System.out.println("ERROR: com.games.potato.mathbyheart.math.Stack is empty");
			return (E) null;
		} else {			
			E topElement = (E) elements[peak];
			peak = peak - 1;
			return topElement;
		}
	}
	
	public boolean empty(){
		return peak ==-1;
	}

	public E look(){
		return(E) elements[peak];
	}


}

