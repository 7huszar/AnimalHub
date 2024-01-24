import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {AnimalService} from "../../services/animal.service";
import {Type} from "../../enums/type";
import {Mode} from "../../enums/modes";
import {FileDto} from "../../dtos/file";
import {ShelterService} from "../../services/shelter.service";

@Component({
  selector: 'app-content',
  templateUrl: './content.component.html',
  styleUrls: ['./content.component.scss']
})
export class ContentComponent implements OnChanges{

  @Input() animalId: string;
  @Input() userId: string;
  @Input() mode: Mode;
  @Input() file: FileDto;
  @Input() setClass: string;

  private type: Type;
  protected content: any;

  constructor(private animalService: AnimalService,
              private shelterService: ShelterService) {
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (this.animalId != null && this.mode != null) {
      if (this.mode === Mode.ANIMAL || this.mode === Mode.ANIMAL_HOME) {
        this.loadAnimalProfileImage(changes.animalId.currentValue);
      } else if (this.mode === Mode.SHELTER || this.mode === Mode.SHELTER_HOME) {
        this.loadShelterProfileImage(changes.animalId.currentValue);
      } else if (this.mode === Mode.CONTENT) {
        this.loadFile(changes.animalId.currentValue);
      }
    }
  }

  private loadAnimalProfileImage(id: string): void {
    this.type = Type.IMAGE;
    this.animalService.getProfileImage(id).subscribe({
      next: blob => {
        this.content = URL.createObjectURL(blob);
      }, error: err => {
        // TODO handle error
        console.error(err);
        this.content = null;
      }
    })
  }

  private loadShelterProfileImage(id: string): void {
    this.type = Type.IMAGE;
    this.shelterService.getProfileImage(id).subscribe({
      next: blob => {
        this.content = URL.createObjectURL(blob);
      }, error: err => {
        // TODO handle error
        console.error(err);
        this.content = null;
      }
    });
  }

  private loadFile(animalId: string): void {
    if (this.file !== undefined) {
      if (this.file.contentMimeType.startsWith('image')) {
        this.type = Type.IMAGE;
      } else if (this.file.contentMimeType.startsWith('video')) {
        this.type = Type.VIDEO;
      }

      this.animalService.getContent(animalId, this.userId, this.file.id).subscribe({
        next: blob => {
          this.content = URL.createObjectURL(blob);
        }, error: err => {
          // TODO handle error
          console.log(err);
        }
      })
    }
  }

  protected isImage(): boolean {
    return this.type===Type.IMAGE;
  }

  protected isVideo(): boolean {
    return this.type===Type.VIDEO;
  }

  protected readonly Mode = Mode;
}
