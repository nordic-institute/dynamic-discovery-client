package org.busdox.servicemetadata.publishing._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "ProcessListType",
        propOrder = {"process"}
)
public class ProcessListType {
    @XmlElement(
            name = "Process",
            required = true
    )
    protected List<ProcessType> process;

    public ProcessListType() {
    }

    public List<ProcessType> getProcess() {
        if(this.process == null) {
            this.process = new ArrayList();
        }

        return this.process;
    }
}
