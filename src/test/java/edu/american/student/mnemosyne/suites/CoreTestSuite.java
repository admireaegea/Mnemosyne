package edu.american.student.mnemosyne.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import edu.american.student.mnemosyne.core.BaseNetworkBuilderProcessTest;
import edu.american.student.mnemosyne.core.HadoopForemanTest;
import edu.american.student.mnemosyne.core.IngestProcessTest;
import edu.american.student.mnemosyne.core.PropertiesLoaderTest;

@RunWith(Suite.class)
@SuiteClasses
({
	PropertiesLoaderTest.class,
	HadoopForemanTest.class,
	IngestProcessTest.class,
	BaseNetworkBuilderProcessTest.class
})
public class CoreTestSuite
{

}
