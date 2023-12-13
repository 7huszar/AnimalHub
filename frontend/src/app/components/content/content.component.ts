import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {AnimalService} from "../../services/animal.service";
import {Type} from "../../enums/type";
import {Mode} from "../../enums/modes";

@Component({
  selector: 'app-content',
  templateUrl: './content.component.html',
  styleUrls: ['./content.component.scss']
})
export class ContentComponent implements OnChanges{

  @Input() id: string;
  @Input() mode: Mode;
  @Input() type: Type;
  @Input() setClass: string;

  protected content: any;

  constructor(private animalService: AnimalService) {
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (this.id != null && this.mode != null && this.type != null) {
      if (this.mode === Mode.ANIMAL) {
        this.loadAnimalProfileImage(changes.id.currentValue);
      } else if (this.mode === Mode.SHELTER) {
        this.loadShelterProfileImage(changes.id.currentValue);
      } else if (this.mode === Mode.FILE) {
        this.loadFile(changes.id.currentValue);
      }
    }
  }

  private loadAnimalProfileImage(id: string): void {
    this.animalService.getProfileImage(id).subscribe({
      next: file => {
        this.content = URL.createObjectURL(file);
      }, error: err => {
        // TODO handle error
      }
    })
  }

  private loadShelterProfileImage(id: string): void {
    // TODO implement
  }

  private loadFile(id: string): void {
    // TODO implement
  }

  protected isImage(): boolean {
    return this.type===Type.IMAGE;
  }

  protected isVideo(): boolean {
    return this.type===Type.VIDEO;
  }
}
