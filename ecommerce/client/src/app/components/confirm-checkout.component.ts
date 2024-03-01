import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Cart, LineItem, Order } from '../models';
import { CartStore } from '../cart.store';
import { Observable } from 'rxjs';
import { ProductService } from '../product.service';

@Component({
  selector: 'app-confirm-checkout',
  templateUrl: './confirm-checkout.component.html',
  styleUrl: './confirm-checkout.component.css'
})
export class ConfirmCheckoutComponent implements OnInit{

  // TODO Task 3
  private fb = inject(FormBuilder);
  private cartStore = inject(CartStore)
  private productSvc = inject(ProductService)

  form!: FormGroup
  cartItems$!: Observable<LineItem[]>
  total: number = 0
  cart: Cart = {
    lineItems: []
  }

  ngOnInit(): void {
    this.form = this.createForm()
    this.cartItems$ = this.cartStore.getAllItems
    this.cartItems$.subscribe({
      next: value => {
        value.forEach(i => {
          this.total+=i.price * i.quantity
          this.cart.lineItems.push(i)
        })
      }
    })
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
    var order = this.form.value as Order //missing cart for now
    order.cart = this.cart
    console.info(order)
    this.productSvc.checkout(order)
  }

}
