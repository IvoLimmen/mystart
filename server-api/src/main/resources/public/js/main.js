var currentSelection = 0;
var oldSelection;
var maxSelection;
var gridSize = 4;
var menuOpen = false;
var editOpen = false;
var selectedMenuItem = 0;

function unselect() {
  if (oldSelection > 0) {
    var oldElement = document.getElementById('content').children[oldSelection];
    if (oldElement != null) {
      oldElement.className = 'box';
    }
    closeMenu();
  }
}

function select() {
  var newElement = document.getElementById('content').children[currentSelection];
  if (newElement != null) {
    newElement.className = 'box selected';
  }
}

function selectMenuItem() {
  var items = document.getElementsByClassName('menuitem');

  for (var i = 0; i < items.length; i++) {
    if (i === selectedMenuItem) {
      items[i].className = 'menuitem selected'
    } else {
      items[i].className = 'menuitem'
    }
  }
}

function openEdit() {
  if (!editOpen) { 
    closeMenu();
    document.getElementById('edit').className = 'edit edit-glide-in';
    editOpen = true;
  }

}

function closeEdit() {
  if (editOpen) { 
    document.getElementById('edit').className = 'edit edit-glide-out';
    editOpen = false;
  }
}

function openMenu() {
  if (!menuOpen) {
    // fill data
    var link = document.getElementById('content').children[currentSelection].link;
    var description = link.description ? link.description : "";
    document.getElementById('link.url').textContent = "URL: " + link.url;
    document.getElementById('link.description').textContent = "Description: " + description;
    document.getElementById('link.labels').textContent = "Labels: " + link.labels;

    document.getElementById('menu').className = 'menu glide-in';
    menuOpen = true;
    selectedMenuItem = 0;
    selectMenuItem();
  }
}

function closeMenu() {
  if (menuOpen) { 
    document.getElementById('menu').className = 'menu glide-out';
    menuOpen = false;
  }
}

function resetSelection() {
  unselect();
  currentSelection = 0;
}

function keyboardInputHandler(keyEvent) {
  var inputHasFocus = (document.activeElement === document.getElementById('command_input'));
  oldSelection = currentSelection;
  maxSelection = document.getElementById('content').children.length;

  if (menuOpen) {
    if (keyEvent.key === 'Enter') {
      var link = document.getElementById('content').children[currentSelection].link;
      if (selectedMenuItem === 0) {
        window.open(link.url, "_BLANK");
        fireVisitLink(link);
      } else if (selectedMenuItem === 1) {
        openEdit();
      } else if (selectedMenuItem === 2) {
        fireDeleteLink(link);
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

  if (!inputHasFocus && keyEvent.key === '/') {
    resetSelection();
    keyEvent.preventDefault();
    document.getElementById('command_input').focus();
    return;  
  } else if (keyEvent.keyCode === 37) {
    // left
    if (currentSelection > 1) {
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
    if (currentSelection === 0) {
      currentSelection = 1;
    } else if ((currentSelection + 1) < maxSelection) {
      currentSelection++;
    } else {
      return;
    }
  } else if (keyEvent.keyCode === 40) {
    // down
    if (currentSelection === 0) {
      currentSelection = gridSize;
    } else if ((currentSelection + gridSize) < maxSelection) {
      currentSelection += gridSize;
    } else {
      return;
    }
  } else if (keyEvent.key === 'Enter') {
    if (inputHasFocus) {
      return;
    }
    openMenu();
    return;
  } else {
    // not navigation
    return;
  }
  document.getElementById('command_input').blur();
  unselect();
  select();
}

function createLink(link) {
  var div = document.createElement('div');
  div.link = link;
  div.className = "box";
  var h1 = document.createElement('h1');
  h1.textContent = link.title;
  div.appendChild(h1);
  var p = document.createElement('p');
  p.textContent = link.description;
  div.appendChild(p);
  document.getElementById('content').appendChild(div);
}

function removeChildrenFromParent(elementId, leaveFirst) {
  var parent = document.getElementById(elementId);
  var children = parent.children;
  var startIndex = leaveFirst ? 1 : 0;

  for (var i = startIndex; i < children.length; i++) {
    parent.removeChild(children[i]);
  }
}

function fireVisitLink(link) {
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function () {
    if (this.readyState == 4 && this.status == 200) {
      closeMenu();
    }
  };
  xhttp.open("PUT", "/api/link/visit?id=" + link.id, true);
  xhttp.send();
}

function fireDeleteLink(link) {
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function () {
    if (this.readyState == 4 && this.status == 200) {
      document.getElementById('content').children[currentSelection].remove();
      unselect();
      currentSelection--;
      select();
    }
  };
  xhttp.open("DELETE", "/api/link/delete?id=" + link.id, true);
  xhttp.send();
}

function commandHandler(keyEvent) {
  if (keyEvent.key === 'Enter') {
    var me = document.getElementById('command_input');
    keyEvent.preventDefault();
    removeChildrenFromParent('content', true);
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
      if (this.readyState == 4 && this.status == 200) {
        var links = JSON.parse(xhttp.responseText);
        for (var i = 0; i < links.length; i++) {          
          createLink(links[i]);
        }
        resetSelection();
      }
    };
    xhttp.open("GET", "/api/link/search?input=" + me.value, true);
    xhttp.send();
  }
}

// bootstrap
(function () {
  document.onkeydown = keyboardInputHandler;
  document.getElementById('command_input').onkeypress = commandHandler;
})();
