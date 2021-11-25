import { LitElement, html, css, customElement } from 'lit-element';
import '@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-horizontal-layout.js';
import '@vaadin/vaadin-combo-box/src/vaadin-combo-box.js';
import '@vaadin/vaadin-text-field/src/vaadin-number-field.js';
import '@vaadin/vaadin-grid/src/vaadin-grid.js';
import '@vaadin/vaadin-grid/src/vaadin-grid-column.js';

@customElement('hc-view')
export class HcView extends LitElement {
  static get styles() {
    return css`
      :host {
          display: block;
          height: 100%;
      }
      `;
  }

  render() {
    return html`
<vaadin-vertical-layout style="width: 100%; height: 100%; padding: var(--lumo-space-xs);" theme="spacing-s">
 <vaadin-horizontal-layout theme="spacing" style="width: 100%; padding: var(--lumo-space-s); align-self: flex-start; flex-grow: 0; flex-shrink: 0;">
  <vaadin-combo-box id="tipoComida" style="flex-grow: 1; align-self: flex-start; padding: var(--lumo-space-s); margin: var(--lumo-space-s); flex-shrink: 1; width: 100%;" placeholder="Elige tipo de comida..."></vaadin-combo-box>
 </vaadin-horizontal-layout>
 <vaadin-horizontal-layout theme="spacing" style="width: 100%; padding: var(--lumo-space-s);">
  <vaadin-combo-box placeholder="Elige alimento..." label="Alimento a contar..." id="alimento"></vaadin-combo-box>
  <vaadin-number-field min="0" value="1" max="8.5" label="RCs" step="0.5">
    Text 
  </vaadin-number-field>
  <vaadin-number-field min="0" value="0" label="Gramos" has-value>
    Text 
  </vaadin-number-field>
 </vaadin-horizontal-layout>
 <vaadin-horizontal-layout theme="spacing" style="padding: var(--lumo-space-s); width: 100%;">
  <vaadin-vertical-layout theme="spacing" style="width: 100%;">
   <vaadin-grid items="[[items]]" id="consumido" style="width: 100%;" all-rows-visible>
    <vaadin-grid-column width="50px" flex-grow="0" id="ali" style="width: 50%;" header="Alimento">
     <template class="header" style="width: 50%;">
      Alimento 
     </template>
     <template id="aliCol" class="Alimento.class">
       [[index]] 
     </template>
    </vaadin-grid-column>
    <vaadin-grid-column id="racs" style="width: 50%;" header="Raciones">
     <template class="header">
      Raciones 
     </template>
     <template id="racsCol" class="Integer">
       [[item.value]] 
     </template>
    </vaadin-grid-column>
   </vaadin-grid>
  </vaadin-vertical-layout>
  <vaadin-vertical-layout theme="spacing"></vaadin-vertical-layout>
 </vaadin-horizontal-layout>
</vaadin-vertical-layout>
`;
  }

  // Remove this method to render the contents of this view inside Shadow DOM
  createRenderRoot() {
    return this;
  }
}
