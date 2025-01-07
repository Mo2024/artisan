import { Component, Input, Output, EventEmitter } from '@angular/core';
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
  @Input() accounts: { id: number, name: string }[] = [];
  accountId: number | null = null
  addedBalance: string = '';

  constructor(private accountsService: AccountsService) { }

  addAccountBalance(addedBalance: string) {
    if (!addedBalance.trim() || this.accountId == null) {
      alert('Added balance and account cannot be empty');
      return; // Exit the function if empty
    }

    if (!this.isValidNumber(addedBalance)) {
      alert('Added balance must be a valid number')
    }

    this.accountsService.addBalance(addedBalance, this.accountId as number).subscribe({
      next: (response) => {
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

  isValidNumber(value: string): boolean {
    // Trim the value to remove any leading/trailing whitespace
    const trimmedValue = value.trim();

    // Convert the trimmed value to a number
    const num = Number(trimmedValue);

    // Check if it is not empty, is not NaN, and is finite
    return trimmedValue !== '' && !isNaN(num) && isFinite(num);
  }


}
