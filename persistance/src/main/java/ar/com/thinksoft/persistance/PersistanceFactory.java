package ar.com.thinksoft.persistance;

import ar.com.thinksoft.persistance.generix.implementations.ImpDomain;
import ar.com.thinksoft.persistance.generix.implementations.ImpDomainExtended;
import ar.com.thinksoft.persistance.generix.interfaces.IntDomain;
import ar.com.thinksoft.persistance.generix.interfaces.IntDomainExtended;

@SuppressWarnings({"checkstyle:FileLength", "checkstyle:ImportControl", "checkstyle:InnerAssignment"})
public class PersistanceFactory {

	/* ******************************************************
	 * ********************** GENERICS **********************
	 * ******************************************************
	 */
	private static IntDomain intDomain;

	public static IntDomain getIntDomain() {
		return intDomain == null ? intDomain = new ImpDomain() : intDomain;
	}

	private static IntDomainExtended intDomainExtendeed;

	public static IntDomainExtended getIntDomainExtendeed() {
		return intDomainExtendeed == null ? intDomainExtendeed = new ImpDomainExtended() : intDomainExtendeed;
	}

}

