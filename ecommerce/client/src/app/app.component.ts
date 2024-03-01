import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import {Observable, Subscription} from 'rxjs';
import {Router} from '@angular/router';
import { CartStore } from './cart.store';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit, OnDestroy{

  // NOTE: you are free to modify this component

  private router = inject(Router)
  private cartStore = inject(CartStore)

  itemCount!: number
  count$!: Subscription

  ngOnInit(): void {
    this.count$ = this.cartStore.getNumberOfItems
    .subscribe({
      next: (data) => {
        console.info(data)
        this.itemCount = data
      }
    })
  }

  ngOnDestroy(): void {
    this.count$.unsubscribe()
  }

  checkout(): void {
    this.router.navigate([ '/checkout' ])
  }
}
