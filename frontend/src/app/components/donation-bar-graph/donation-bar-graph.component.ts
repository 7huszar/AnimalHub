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
  private animals: DonationSumDto[];

  private thickness: number = 30;
  private gap: number = 50;

  private visibleDataCount = 5;

  height: number;

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
    this.calculateHeight();
  }

  private updateChart() {
    const data = this.animals.slice(0, this.visibleDataCount).reverse();
    this.chartOptions = {
      grid: {
        top: 0,     // No padding on the top
        bottom: 0,  // No padding on the bottom
        containLabel: false // Set this to false if you handle label positioning manually
      },
      xAxis: {
        type: 'value',
      },
      yAxis: {
        type: 'category',
        data: data.map(item => item.name),
      },
      series: [
        {
          type: 'bar',
          barWidth: this.thickness,
          barGap: this.gap,
          data: data.map(item => item.amount),
        },
      ],
    };
  }

  expandChart() {
    this.visibleDataCount = Math.min(this.visibleDataCount + 5, this.animals.length);
    this.calculateHeight();
    this.updateChart();
  }

  reduceChart(): void {
    this.visibleDataCount = Math.max(this.visibleDataCount - 5, 0);
    this.calculateHeight();
    this.updateChart();
  }

  private calculateHeight(): void {
    this.height = (this.thickness + this.gap * this.thickness / 100) * this.visibleDataCount;

  }
}
