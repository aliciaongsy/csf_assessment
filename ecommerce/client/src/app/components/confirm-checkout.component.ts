import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Cart, LineItem, Order } from '../models';
import { CartStore } from '../cart.store';
import { Observable, Subscription } from 'rxjs';
import { ProductService } from '../product.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-confirm-checkout',
  templateUrl: './confirm-checkout.component.html',
  styleUrl: './confirm-checkout.component.css'
})
export class ConfirmCheckoutComponent implements OnInit, OnDestroy{

  // TODO Task 3
  private fb = inject(FormBuilder);
  private cartStore = inject(CartStore)
  private productSvc = inject(ProductService)
  private router = inject(Router)

  form!: FormGroup
  cartItems$!: Observable<LineItem[]>
  total: number = 0
  cart: Cart = {
    lineItems: []
  }
  cartSub$!:Subscription
  checkoutSub$!: Subscription

  ngOnInit(): void {
    this.form = this.createForm()
    this.cartItems$ = this.cartStore.getAllItems
    this.cartSub$ = this.cartItems$.subscribe({
      next: value => {
        value.forEach(i => {
          // calculate total price
          this.total+=i.price * i.quantity
          console.info(i)
          // add line items into cart 
          this.cart.lineItems.push(i)
        })
      }
    })
  }

  ngOnDestroy(): void {
    this.cartSub$.unsubscribe()
    if (this.checkoutSub$!=undefined){
      this.checkoutSub$.unsubscribe()
    }
  }

  createForm(): FormGroup {
    return this.fb.group({
      name: this.fb.control<string>('', [Validators.required]),
      address: this.fb.control<string>('', [Validators.required, Validators.minLength(3)]),
      priority: this.fb.control<boolean>(false),
      comments: this.fb.control<string>('')
    })
  }

  submitOrder(){
    var order = this.form.value as Order
    order.cart = this.cart
    console.info(order)
    this.checkoutSub$ = this.productSvc.checkout(order).subscribe({
      next: (value) => {
        alert(`orderId: ${value.orderId}`)
        // clear component store on successful checkout
        this.cartStore.clearCart()
        this.router.navigate(['/'])
      },
      error: (err) => {
        alert(`error: ${err.message}`)
      }
    })
  }

}
