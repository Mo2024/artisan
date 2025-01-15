import { Component, Input, NO_ERRORS_SCHEMA } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AccountsService } from '../services/accounts.service';
import { CashService } from '../services/cash.service';
import { MatTabsModule } from '@angular/material/tabs';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-account-transactions',
  standalone: true,
  imports: [MatTabsModule, CommonModule],
  // schemas: [NO_ERRORS_SCHEMA],
  templateUrl: './account-transactions.component.html',
  styleUrl: './account-transactions.component.css'
})
export class AccountTransactionsComponent {
  accountId: string | null = null;
  transactions!: any[]
  deposits!: any[]
  account!: any;


  constructor(private route: ActivatedRoute, private cashService: CashService) { }

  ngOnInit(): void {
    // Access the route parameter
    this.accountId = this.route.snapshot.paramMap.get('accountId');
    this.getAccountTransactions();
  }

  getAccountTransactions() {
    this.cashService.getTransactionsByAccountId(this.accountId as string).subscribe({
      next: (response) => {
        this.transactions = response.transactions;
        this.deposits = response.deposits;
        this.account = response.account
      },
      error: (error) => {
        if (error.error.error) {
          alert(error.error.error)
        } else {
          alert('unknown error occured')
        }
      }
    });
  }

  viewDetails(deposit: any) {

  }
}

