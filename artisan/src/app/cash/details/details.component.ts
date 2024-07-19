import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CashService } from '../../services/cash.service';
import { SitesService } from '../../services/sites.service';
import { format } from 'date-fns';

@Component({
  selector: 'app-details',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './details.component.html',
  styleUrl: './details.component.css'
})
export class DetailsComponent {
  isEditing: boolean = false;
  @Output() closeClicked: EventEmitter<void> = new EventEmitter<void>();
  @Output() cashEdited: EventEmitter<any> = new EventEmitter();
  @Input() cash: any;
  @Input() sites: any;
  @Input() suppliers!: Array<any>;
  @Input() accounts!: Array<any>;

  date: string = '';
  paid_by: string = '';
  payment_method!: string;
  cost: string = '';
  description: string = '';
  site_id!: string;
  date_recorded: string = '';
  date_edited: string = '';
  account_id: any
  selectedAccountIndex!: number

  constructor(private cashService: CashService, private sitesService: SitesService) { }


  ngOnInit(): void {
    this.date = this.cash.date
    this.paid_by = this.cash.paid_by
    this.payment_method = this.cash.payment_method
    this.cost = this.cash.cost
    this.description = this.cash.description
    this.site_id = this.cash.site_id
    this.date_recorded = this.cash.date_recorded
    this.date_edited = this.cash.date_edited
    this.account_id = this.cash.account_id

    for (let i = 0; i < this.accounts.length; i++) {
      if (this.accounts[i].id === this.account_id) {
        this.selectedAccountIndex = i;
        break;
      }
    }
  }


  emitCloseClicked(): void {
    this.closeClicked.emit();
  }

  editCash() {
    if (!this.date.trim() || !this.paid_by.trim() || !this.payment_method || !this.site_id || !this.description.trim()) {
      alert('All fields must be filled out');
      return;
    }

    if (isNaN(parseFloat(this.cost)) || parseFloat(this.cost) <= 0) {
      alert('Cost must be a valid number greater than one');
      return;
    }

    if (this.payment_method == 'bank account' && this.selectedAccountIndex) {
      this.account_id = this.accounts[this.selectedAccountIndex].id
      if (this.cost > this.accounts[this.selectedAccountIndex].balance) {
        alert('No enough balance!');
        return
      }

    } else {
      this.account_id = null
    }

    this.cashService.editCash(this.cash.id, this.date, this.paid_by, this.payment_method, this.site_id, this.cost, this.description, this.account_id).subscribe({
      next: (response) => {
        console.log('Site added:', response);
        const currentDate = new Date();
        const formattedDate = format(currentDate, 'dd-MM-yyyy');
        this.date_edited = formattedDate;
        this.cash.date_edited = formattedDate;
        this.cashEdited.emit(response);
        this.toggleEditCash()
      },
      error: (error) => {
        console.error('Error adding site:', error);
      }
    });




  }

  toggleEditCash() {
    this.isEditing = !this.isEditing
  }

  deleteCash(cash: any) {
    const { id } = cash;
    this.cashService.deleteCash(id).subscribe({
      next: (response) => {
        this.cashEdited.emit(response);
        this.closeClicked.emit();
      },
      error: (error) => {
        console.error('Error deleting supplier:', error);
      }
    });
  }

}
