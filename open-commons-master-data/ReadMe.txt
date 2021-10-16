https://springbootdev.com/2017/11/13/what-are-the-uses-of-entityscan-and-enablejparepositories-annotations/

Read above for the following annotations applied on MasterDataApp class
@Configuration
@EnableConfigurationProperties({ MasterData.class })
@EnableJpaRepositories(basePackages = "org.openframework.commons.masterdata")
@EntityScan(basePackageClasses = MasterDataApp.class)
