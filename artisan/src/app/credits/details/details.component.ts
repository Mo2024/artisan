import { CommonModule, DatePipe } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SitesService } from '../../services/sites.service';
import { CreditsService } from '../../services/credits.service';
import { format } from 'date-fns';

@Component({
  selector: 'app-details',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './details.component.html',
  styleUrl: './details.component.css'
})
export class DetailsComponent {
  isChoosingCreditor: boolean = false;
  @Output() closeClicked: EventEmitter<void> = new EventEmitter<void>();
  @Output() creditEdited: EventEmitter<any> = new EventEmitter();
  @Input() credit: any;
  @Input() sites: any;
  @Input() suppliers!: Array<any>;
  @Input() accounts!: Array<any>;

  date: string = '';
  invoice_no: string = '';
  supplier_id!: number;
  cost: string = '';
  description: string = '';
  site_id: string = '';
  creditId: string = '';
  date_recorded: string = '';
  date_edited: string = '';
  isPaid!: boolean;
  accountId: number | null = null

  constructor(private creditsService: CreditsService, private sitesService: SitesService) { }


  ngOnInit(): void {
    this.date = this.credit.date
    this.invoice_no = this.credit.invoiceNo
    this.supplier_id = this.credit.creditor.id
    this.cost = this.credit.cost
    this.description = this.credit.description
    this.site_id = this.credit.site.id
    this.date_recorded = this.credit.dateRecorded
    this.date_edited = this.credit.dateEdited
    this.isPaid = this.credit.isPaid;
    this.creditId = this.credit.id;
  }


  emitCloseClicked(): void {
    this.closeClicked.emit();
  }

  togglePayCreditor() {
    this.isChoosingCreditor = !this.isChoosingCreditor
  }

  submitPayCreditor() {
    this.creditsService.payCredit(this.date, this.site_id, this.cost, this.description, this.accountId, this.creditId).subscribe({
      next: (response) => {
        this.togglePayCreditor()
        this.creditEdited.emit(response);
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

  deleteCredit(credit: any) {
    const { id } = credit;
    this.creditsService.deleteCredit(id).subscribe({
      next: (response) => {
        this.creditEdited.emit(response);
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
