from datetime import datetime
from typing import Optional


class Person:
    def __init__(
        self,
        id: int = 0,
        name: str = "",
        surname: str = "",
        registrationDate: Optional[datetime] = None,
    ) -> None:
        self.id = id
        self.name = name
        self.surname = surname
        self.registrationDate = registrationDate

    def toString(self, longFormat: bool) -> str:
        if longFormat:
            return (
                "\nPerson (id: "
                + str(self.id)
                + ",\n"
                + "name: "
                + str(self.name)
                + ",\n"
                + "surname: "
                + str(self.surname)
                + ",\n"
                + "registrationDate: "
                + str(self.registrationDate)
                + ")\n"
            )
        else:
            return "Person (id: " + str(self.id) + ")"

