package it.intesys.codylab.rookie.commandline;

import java.io.IOException;
import java.util.List;

public interface InterfacciaServizioPersone {
    void process(List<Person> person) throws IOException;
}
