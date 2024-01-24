import {Component, Input, OnInit} from '@angular/core';
import {EChartsOption} from "echarts";
import {DonationSumDto, ShelterDetailDto} from "../../dtos/shelter";
import {DonationService} from "../../services/donation.service";

@Component({
  selector: 'app-donation-bar-graph',
  templateUrl: './donation-bar-graph.component.html',
  styleUrls: ['./donation-bar-graph.component.scss']
})
export class DonationBarGraphComponent implements OnInit {

  @Input() shelter: ShelterDetailDto = null;

  chartOptions: EChartsOption;

  private data = [
    { name: 'Item A', value: 120 },
    { name: 'Item B', value: 200 },
    { name: 'Item C', value: 150 },
    { name: 'Item D', value: 80 },
    { name: 'Item E', value: 70 },
    { name: 'Item F', value: 110 },
    { name: 'Item G', value: 130 },
    { name: 'Item H', value: 210 },
    { name: 'Item I', value: 140 },
    { name: 'Item J', value: 170 },
    { name: 'Item K', value: 100 },
    { name: 'Item L', value: 90 }
  ];

  private animals: DonationSumDto[];

  private visibleDataCount = 5;

  constructor(
    private donationService: DonationService
  ) {
  }

  ngOnInit() {
    this.donationService.getDonationByReceiver(this.shelter.id).subscribe({
      next: next => {
        this.animals = next;
        console.log(typeof this.animals[0].amount); // Check the type of the first item's amount
        console.log(next);
        this.updateChart();
      },
      error: error => {
        console.error(error);
      }
    });
    console.log();
  }

  private updateChart() {
    this.chartOptions = {
      xAxis: {
        type: 'value',
      },
      yAxis: {
        type: 'category',
        data: this.animals.slice(0, this.visibleDataCount).map(item => item.name),
      },
      series: [
        {
          type: 'bar',
          data: this.animals.slice(0, this.visibleDataCount).map(item => item.amount),
        },
      ],
    };
  }

  expandChart() {
    this.visibleDataCount = Math.min(this.visibleDataCount + 5, this.animals.length);
    this.updateChart();
  }
}
