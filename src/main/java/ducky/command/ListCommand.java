package ducky.command;

import ducky.chore.ChoreList;
import ducky.storage.Storage;
import ducky.ui.Ui;

/** Shows every chore currently in the chore list. */
public class ListCommand extends Command {
    @Override
    public void execute(ChoreList chores, Ui ui, Storage storage) {
        ui.showChores(chores);
    }
}
