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
  payment_method: string = '';
  cost: string = '';
  description: string = '';
  site_id!: string;
  date_recorded: string = '';
  date_edited: string = '';
  account_id: any
  selectedAccountIndex!: number
  isCredit!: boolean

  constructor(private cashService: CashService, private sitesService: SitesService) { }


  ngOnInit(): void {
    this.date = this.cash.date
    this.paid_by = this.cash.paidBy
    this.payment_method = this.cash.paymentMethod
    this.cost = this.cash.cost
    this.description = this.cash.description
    this.site_id = this.cash.site.id
    this.date_recorded = this.cash.dateRecorded
    this.date_edited = this.cash.dateEdited
    this.account_id = this.cash.account.id
    this.isCredit = this.cash.isCredit


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
        if (error.error.error) {
          alert(error.error.error)
        } else {
          alert('unknown error occured')
        }
      }
    });
  }

}
