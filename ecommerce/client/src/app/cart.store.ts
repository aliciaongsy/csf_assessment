
// TODO Task 2

import { Injectable } from "@angular/core";
import { ComponentStore } from "@ngrx/component-store";
import { Cart, LineItem } from "./models";

const INIT_STATE = {
    lineItems: []
}

// Use the following class to implement your store
@Injectable()
export class CartStore extends ComponentStore<Cart>{

    constructor(){
        super(INIT_STATE)
    }

    readonly addItem = this.updater<LineItem>(
        (slice: Cart, item: LineItem) => {
            console.info("adding item to store")
            for (let i = 0; i < slice.lineItems.length; i++){
                if (slice.lineItems[i].prodId == item.prodId){
                    console.info("same item, increment count")
                    slice.lineItems[i].quantity+=1
                    return {
                        // return current slice
                        lineItems: slice.lineItems
                    }
                }
                if (i == slice.lineItems.length){
                    // item does not exist in cart
                    console.info("adding new item")
                    return {
                        lineItems: [...slice.lineItems, item]
                    }
                }
            }
            return {
                lineItems: [...slice.lineItems, item]
            }
        }
    )

    readonly getNumberOfItems = this.select<number>(
        (slice: Cart) => slice.lineItems.length
    )

    readonly getAllItems = this.select<LineItem[]>(
        (slice: Cart) => slice.lineItems
    )

    readonly clearCart = this.updater<void>(
        (slice: Cart) => {
            console.info("clearing db")
            this.setState(INIT_STATE)
            return {
                lineItems: slice.lineItems
            }
        }
    )
}
