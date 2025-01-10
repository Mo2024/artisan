import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AccountsService } from '../services/accounts.service';
import { CashService } from '../services/cash.service';

@Component({
  selector: 'app-account-transactions',
  standalone: true,
  imports: [],
  templateUrl: './account-transactions.component.html',
  styleUrl: './account-transactions.component.css'
})
export class AccountTransactionsComponent {
  accountId: string | null = null;
  transactions!: any[]

  constructor(private route: ActivatedRoute, private cashService: CashService) { }

  ngOnInit(): void {
    // Access the route parameter
    this.accountId = this.route.snapshot.paramMap.get('accountId');
    this.getAccountTransactions();
  }

  getAccountTransactions() {
    this.cashService.getTransactionsByAccountId(this.accountId as string).subscribe({
      next: (response) => {
        this.transactions = response;
        console.log(response)
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
}

