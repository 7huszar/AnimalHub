import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-animal-card',
  templateUrl: './animal-card.component.html',
  styleUrls: ['./animal-card.component.scss']
})
export class AnimalCardComponent implements OnInit{

  @Input() name :string = "Schnappi das kleine Krokodil";
  @Input() id :number | unknown;
  like() :void {
    console.log("like");
  }

  dislike() :void {
    console.log("dislike")
  }

  ngOnInit(): void {
  }
}
