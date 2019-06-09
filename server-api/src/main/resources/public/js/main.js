var currentSelection = 0;
var oldSelection;
var maxSelection;
var gridSize = 3;

function unselect() {
  if (oldSelection > 0) {
    var oldElement = document.getElementById('content').children[oldSelection];
    if (oldElement != null) {
      oldElement.className = 'box';
    }
  }
}

function select() {
  var newElement = document.getElementById('content').children[currentSelection];
  if (newElement != null) {
    newElement.className = 'box selected';
  }
}

function navigationHandler(keyEvent) {
  oldSelection = currentSelection;
  maxSelection = document.getElementById('content').children.length;

  if (keyEvent.keyCode === 37) {
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
  } else {
    // not navigation
    return;
  }
  document.getElementById('command_input').blur();
  unselect();
  select();
}

function focusHandler(keyEvent) {
  if (document.activeElement === document.getElementById('command_input')) {
    return;
  }
  if (keyEvent.key === '/') {
    keyEvent.preventDefault();
    document.getElementById('command_input').focus();
  }
}

function commandHandler(keyEvent) {
  if (keyEvent.key === 'Enter') {
    var me = document.getElementById('command_input');
    keyEvent.preventDefault();
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
      if (this.readyState == 4 && this.status == 200) {
        var div = document.createElement('div');
        div.textContent = xhttp.responseText;
        div.className = "box";
        document.getElementById('content').appendChild(div);
        currentSelection = 0;
        unselect();
      }
    };
    xhttp.open("GET", "/command?input=" + me.value, true);
    xhttp.send();
  }
}

// bootstrap
(function () {
  document.onkeypress = focusHandler;
  document.onkeydown = navigationHandler;
  document.getElementById('command_input').onkeypress = commandHandler;
})();
