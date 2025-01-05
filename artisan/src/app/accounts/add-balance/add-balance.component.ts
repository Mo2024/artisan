import { Component, Input, Output } from '@angular/core';
import { EventEmitter } from 'stream';
import { AccountsService } from '../../services/accounts.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-add-balance',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './add-balance.component.html',
  styleUrl: './add-balance.component.css'
})
export class AddBalanceComponent {
  @Output() closeClicked: EventEmitter<void> = new EventEmitter<void>();
  @Output() balanceAdded: EventEmitter<any> = new EventEmitter();
  @Input() accountId!: number

  addedBalance: string = '';

  constructor(private accountsService: AccountsService) { }

  addAccountBalance(addedBalance: string, accountId: number) {
    if (!addedBalance.trim()) {
      alert('Name and description cannot be empty');
      return; // Exit the function if empty
    }

    this.accountsService.addBalance(addedBalance, accountId).subscribe({
      next: (response) => {
        console.log('balance added:', response);
        this.balanceAdded.emit(response);
        this.closeClicked.emit();
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

  emitCloseClicked(): void {
    this.closeClicked.emit();
  }

}
