var currentSelection = 0;
var oldSelection;
var maxSelection;
var gridSize = 4;
var menuOpen = false;

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

function openMenu() {
  if (!menuOpen) {
    document.getElementById('menu').className = 'menu glide-in';
    menuOpen = true;
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
    // enter on link
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
  div.textContent = link.title;
  div.link = link;
  div.className = "box";
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
