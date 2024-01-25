import {Directive, HostListener} from '@angular/core';

@Directive({
  selector: '[allowOnlyNumber]'
})

export class OnlyNumberDirective {

  @HostListener('keydown', ['$event'])
  onKeyDown(event: KeyboardEvent) {
    if (['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'Backspace', 'Tab', 'ArrowLeft', 'ArrowRight', 'Delete'].indexOf(event.key) === -1) {
      event.preventDefault();
    }
  }
}
