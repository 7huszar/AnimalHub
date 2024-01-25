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
  animals: DonationSumDto[];

  private thickness: number = 30;
  private gap: number = 50;
  private overflow: number = 0;
  private base: number = 5;

  private visibleDataCount = this.base;

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
        top: 0,
        bottom: 0,
        containLabel: false
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
    this.visibleDataCount = Math.min(this.visibleDataCount + this.base, this.animals.length);
    this.overflow = this.visibleDataCount % this.base;
    this.calculateHeight();
    this.updateChart();
  }

  reduceChart(): void {
    if (this.visibleDataCount <= 5) {
      return;
    }
    this.visibleDataCount = Math.max(this.visibleDataCount - (this.overflow == 0 ? this.base : this.overflow), 0);
    this.overflow = this.visibleDataCount % this.base;
    this.calculateHeight();
    this.updateChart();
  }

  private calculateHeight(): void {
    this.height = (this.thickness + this.gap * this.thickness / 100) * this.visibleDataCount;
  }

  isMore(): boolean {
    return this.visibleDataCount != this.animals.length;
  }

  isLess(): boolean {
    return this.visibleDataCount > this.base;
  }

  total(): number {
    return this.animals.length;
  }

  actual(): number {
    return this.visibleDataCount;
  }
}
