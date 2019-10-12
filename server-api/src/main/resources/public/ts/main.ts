var currentSelection: number = -1;
var oldSelection: number;
var maxSelection: number;
const gridSize: number = 4;
var searchOpen: boolean = false;
var menuOpen: boolean = false;
var editOpen: boolean = false;
var labelMode: boolean = false;
var selectedMenuItem: number = 0;

function unselect() {
  if (oldSelection >= 0) {
    var oldElement = document.getElementById('content').children[oldSelection];
    if (oldElement != null) {
      oldElement.classList.remove('selected');
    }
    closeMenu();
  }
}

function select() {
  if (currentSelection >= 0) {
    var newElement = document.getElementById('content').children[currentSelection];
    if (newElement != null) {
      newElement.classList.add('selected');
    }
  }
}

function selectMenuItem() {
  var items = null;
  if (menuOpen) {
    if (labelMode) {
      items = document.querySelectorAll('#labelmenu .menuitem');
    } else {
      items = document.querySelectorAll('#menu .menuitem');
    }
  } else if (editOpen) {
    items = document.querySelectorAll('#edit .menuitem');
  }

  for (var i = 0; i < items.length; i++) {
    if (i === selectedMenuItem) {
      items[i].classList.add('selected');
    } else {
      items[i].classList.remove('selected');
    }
  }
}

function openEdit() {
  if (!editOpen) {
    closeMenu();
    glideIn('edit');
    editOpen = true;
    selectedMenuItem = 0;
    selectMenuItem();

    // fill form
    var link : Link = (document.getElementById('content').children[currentSelection] as any).link;
    var description = link.description ? link.description : "";
    (<HTMLInputElement>document.getElementById('edit.title')).value = link.title;
    (<HTMLInputElement>document.getElementById('edit.url')).value = link.url;
    (<HTMLInputElement>document.getElementById('edit.description')).value = description;
    (<HTMLInputElement>document.getElementById('edit.labels')).value = link.labels as any;
    (<HTMLInputElement>document.getElementById('edit.url')).focus();
  }
}

function closeEdit() {
  if (editOpen) {
    glideOut('edit');
    editOpen = false;
  }
}

function openMenu() {
  if (!menuOpen) {
    if (labelMode) {
      var label = (document.getElementById('content').children[currentSelection] as any).label;

      glideIn('labelmenu');
    } else {
      // fill data
      var link: Link = (document.getElementById('content').children[currentSelection] as any).link;
      var description = link.description ? link.description : "";
      document.getElementById('link.url').textContent = "URL: " + link.url;
      document.getElementById('link.description').textContent = "Description: " + description;
      document.getElementById('link.labels').textContent = "Labels: " + link.labels;

      glideIn('menu');
    }
    menuOpen = true;
    selectedMenuItem = 0;
    selectMenuItem();
  }
}

function closeMenu() {
  if (menuOpen) {
    if (labelMode) {
      glideOut('labelmenu');
    } else {
      glideOut('menu');
    }
    menuOpen = false;
  }
}

function glideOut(name: string) {
  document.getElementById(name).classList.add(name + '-glide-out');
  document.getElementById(name).classList.remove(name + '-glide-in');
}

function glideIn(name: string) {
  document.getElementById(name).classList.remove(name + '-glide-out');
  document.getElementById(name).classList.add(name + '-glide-in');
}

function openSearch() {
  if (!searchOpen) {
    glideIn('search');
    searchOpen = true;
    document.getElementById('command_input').focus();
  }
}

function closeSearch() {
  if (searchOpen) {
    glideOut('search');
    searchOpen = false;
  }
}

function resetSelection() {
  unselect();
  currentSelection = 0;
  select();
}

function keyboardInputHandler(keyEvent: KeyboardEvent) {
  if (editOpen) {
    if (keyEvent.key === 'Enter') {
      var link: Link = (document.getElementById('content').children[currentSelection] as any).link;
      if (selectedMenuItem === 0) {
        link.url = (<HTMLInputElement>document.getElementById('edit.url')).value;
        link.description = (<HTMLInputElement>document.getElementById('edit.description')).value;
        link.setLabelsFromString((<HTMLInputElement>document.getElementById('edit.labels')).value);
        link.title = (<HTMLInputElement>document.getElementById('edit.title')).value;
        fireUpdateLink(link);
        return;
      } else if (selectedMenuItem === 1) {
        closeEdit();
        return;
      }
    } else if (keyEvent.keyCode === 39) {
      // right
      if (selectedMenuItem < 1) {
        selectedMenuItem++;
      }
      selectMenuItem();
    } else if (keyEvent.keyCode === 37) {
      // left
      if (selectedMenuItem > 0) {
        selectedMenuItem--;
      }
      selectMenuItem();
    } else if (keyEvent.keyCode === 27) {
      // esc
      closeEdit();
    }

    return;
  }

  if (menuOpen) {
    if (keyEvent.key === 'Enter') {
      if (labelMode) {
        var label = (document.getElementById('content').children[currentSelection] as any).label;
        if (selectedMenuItem === 0) {
          // todo: show all links from this label
          fireGetByLabel(label);
          return;
        } else if (selectedMenuItem === 1) {
          // todo: move the links from this label to another
          return;
        } else if (selectedMenuItem === 2) {
          // todo: delete the label
          return;
        }
      } else {
        var link: Link = (document.getElementById('content').children[currentSelection] as any).link;
        if (selectedMenuItem === 0) {
          window.open(link.url, "_BLANK");
          fireVisitLink(link);
          return;
        } else if (selectedMenuItem === 1) {
          openEdit();
          return;
        } else if (selectedMenuItem === 2) {
          fireDeleteLink(link);
          return;
        }
      }
    } else if (keyEvent.keyCode === 39) {
      // right
      if (selectedMenuItem < 2) {
        selectedMenuItem++;
      }
      selectMenuItem();
    } else if (keyEvent.keyCode === 37) {
      // left
      if (selectedMenuItem > 0) {
        selectedMenuItem--;
      }
      selectMenuItem();
    } else if (keyEvent.keyCode === 27 || keyEvent.keyCode === 38 || keyEvent.keyCode === 40) {
      // esc, up or down
      closeMenu();
    }

    return;
  }

  if (searchOpen) {
    if (keyEvent.keyCode === 27) {
      closeSearch();
      return;
    }
    // entering text
    return;
  } else if (!searchOpen) {
    // select a link
    oldSelection = currentSelection;
    maxSelection = document.getElementById('content').children.length;

    if (keyEvent.key === '/') {
      resetSelection();
      keyEvent.preventDefault();
      openSearch();
      return;
    } else if (keyEvent.key === 'Enter') {
      keyEvent.preventDefault();
      openMenu();
      return;
    } else if (keyEvent.key === '?') {
      // open help
    } else if (keyEvent.key === 'l') {
      fireGetLabels();
      return;
    } else if (keyEvent.keyCode === 37) {
      // left
      if (currentSelection > 0) {
        currentSelection--;
      } else {
        return;
      }
    } else if (keyEvent.keyCode === 38) {
      // up
      if ((currentSelection - gridSize) > 0) {
        currentSelection -= gridSize;
      } else {
        return;
      }
    } else if (keyEvent.keyCode === 39) {
      // right
      if ((currentSelection + 1) < maxSelection) {
        currentSelection++;
      } else {
        return;
      }
    } else if (keyEvent.keyCode === 40) {
      // down
      if ((currentSelection + gridSize) < maxSelection) {
        currentSelection += gridSize;
      } else {
        return;
      }
    }
    document.getElementById('command_input').blur();
    unselect();
    select();
  }
}

function createLink(link: Link) {
  var div = document.createElement('div') as any;
  div.link = link;
  div.className = "box";
  var h1 = document.createElement('h1');
  h1.textContent = link.title;
  div.appendChild(h1);
  var p = document.createElement('p');
  p.textContent = link.labels as any;
  div.appendChild(p);
  document.getElementById('content').appendChild(div as any);
}

function createLabel(label: string, value: string) {
  var div = document.createElement('div') as any;
  div.label = label;
  div.className = "box";
  var h1 = document.createElement('h1');
  h1.textContent = label;
  div.appendChild(h1);
  var p = document.createElement('p');
  p.textContent = value;
  div.appendChild(p);
  document.getElementById('content').appendChild(div);
}

function removeChildrenFromParent(elementId: string) {
  var parent = document.getElementById(elementId);

  while (parent.hasChildNodes()) {
    parent.removeChild(parent.lastChild);
  }
}

function fireVisitLink(link: Link) {
  fetch("/api/link/visit?id=" + link.id, {
    method: 'GET'
  })
  .then(function (response) {
      closeMenu();
  })
  .catch(function (err) {
    console.log(err);
  });   
}

function fireUpdateLink(link: Link) {
  var data: Link = new Link();
  data.id = link.id;
  data.url = link.url;
  data.description = link.description;
  data.labels = link.labels;
  data.title = link.title;

  fetch("/api/link/update", { 
    method: 'PUT',
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
    body: JSON.stringify(data)
  })
  .then(function (response) {
      // TODO: update
      closeEdit();
  })
  .catch(function (err) {
    console.log(err);
  });    
}

function fireDeleteLink(link: Link) {
  fetch("/api/link/delete?id=" + link.id, { 
    method: 'DELETE'
  })
  .then(function (response) {
    document.getElementById('content').children[currentSelection].remove();
    unselect();
    currentSelection--;
    select();
  })
  .catch(function (err) {
    console.log(err);
  });    
}

function fireGetByLabel(label: string) {
  fetch("/api/link/by_label?label=" + label, {
    method: 'GET'
  })
  .then(response => response.json())
  .then(function (links) {      
    removeChildrenFromParent('content');
    for (var i = 0; i < links.length; i++) {
      createLink(links[i]);
    }
    resetSelection();
    labelMode = false;
    closeSearch();
  })
  .catch(function (err) {
    console.log(err);
  });      
}

function fireGetLabels() {
  removeChildrenFromParent('content');

  fetch("/api/link/by_label", {
    method: 'GET'
  })
  .then(response => response.json())
  .then(function (labels) {
    for (var property in labels) {
      createLabel(property, labels[property]);
    }
    resetSelection();
    labelMode = true;
  })
  .catch(function (err) {
    console.log(err);
  });    
}

function searchHandler(keyEvent: KeyboardEvent) {
  if (keyEvent.key === 'Enter') {    
    var me = document.getElementById('command_input') as any;
    keyEvent.preventDefault();
    removeChildrenFromParent('content');

    fetch("/api/link/search?input=" + me.value, {
      method: 'GET'
    })
    .then(response => response.json())
    .then(function (links) {      
      for (var i = 0; i < links.length; i++) {
        createLink(links[i]);
      }
      resetSelection();
      labelMode = false;
      closeSearch();
    })
    .catch(function (err) {
      console.log(err);
    });    
  }
}

function disableEnter(keyEvent: KeyboardEvent) {
  if (keyEvent.key === 'Enter') {
    keyEvent.preventDefault();
    return;
  }
}

// bootstrap
(function () {
  document.onkeydown = keyboardInputHandler;
  document.getElementById('command_input').onkeypress = searchHandler;
  document.getElementById('edit.url').onkeypress = disableEnter;
  document.getElementById('edit.title').onkeypress = disableEnter;
  document.getElementById('edit.description').onkeypress = disableEnter;
  document.getElementById('edit.labels').onkeypress = disableEnter;
})();
