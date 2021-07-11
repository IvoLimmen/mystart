class Link {
  id: number;
  description: string;
  title: string;
  url: string;
  labels: string[];

  setLabelsFromString(lbls: string) {
    if (lbls.indexOf(",") !== -1) {
      var lbl = lbls.split(',');
      for (var i = 0; i < lbl.length; i++) {
        this.labels.push(lbl[i]);
      }
    } else {
      this.labels.push(lbls);
    }
  }
}

class Label {
  label: string;
  count: number;
}

class User {
  id: number;
  autoStartLabel: string;
  avatarFileName: string;
  email: string;
  fullName: string;
  menuLabels: Array<string>;
  openNewTab: boolean;
}