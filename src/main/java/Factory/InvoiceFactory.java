package Factory;

import Model.Invoice;
import Provider.InvoiceProvider;
import Provider.JpInvoiceProvider;
import Provider.TwInvoiceProvider;
import Provider.UsInvoiceProvider;
import utils.NationCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvoiceFactory {

    Map<NationCode, InvoiceProvider> providerMap = new HashMap<>();

    public InvoiceFactory(){
        register(new TwInvoiceProvider());
        register(new JpInvoiceProvider());
        register(new UsInvoiceProvider());
    }

    private void register(InvoiceProvider provider){
        providerMap.put(provider.getInvoiceType(), provider);
    }

    public Invoice createInvoice(NationCode nationCode, String stateCode){
        InvoiceProvider provider = providerMap.get(nationCode);
        return provider.createInvoice(stateCode);
    }

}
