package it.intesys.codylab.rookie.commandline;

import it.intesys.codylab.rookie.service.RookieService;

import java.io.IOException;
import java.util.List;

public interface PersonService extends RookieService {
    void process(List<Person> person) throws IOException;
}
