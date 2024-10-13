sealed class BottomNavItem(var title: String, var icon: Int, var screen_route: String) {
    object Home : BottomNavItem("Home", R.drawable.ic_home,"home")
    object Search: BottomNavItem("Search", R.drawable.ic_search,"search")
    object Library: BottomNavItem("Library", R.drawable.ic_library, "library")
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination=BottomNavItem.Home.screen_route) {
        composable(BottomNavItem.Home.screen_route) {
            HomeScreen()
        }

        composable(BottomNavItem.Search.screen_route) {
            SearchScreen()
        }

        composable(BottomNavItem.Library.screen_route) {
            LibraryScreen()
        }
    }
}

@Composable
fun BottomNavigation(
    backgroundColor: Color,
    navController: NavController
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Library
    )

    BottomNavigation(
        backgroundColor=colorResource(id=R.color.teal_200)
        contentColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item -> 
            BottomNavigationItem(
                icon={ Icon(
                    imageVector=item.icon, 
                    contentDescription=item.title, 
                    tint=if(currentRoute == item.screen_route ) Color.DarkGrey
                    else Color.LightGrey)
                },
                label={ Text(
                    text=item.title,
                    textAlign=TextAlign.Center,
                    fontSize=9.dp,
                    color=if(currentRoute==item.screen_route) Color.DarkGrey
                    else Color.LightGrey) 
                },
                selectedContentColor=MaterialTheme.colors.secondary,
                alwaysShowLabel=false,
                selected=currentRoute == item.screen_route,
                onClick = {
                    navController.navigate(item.screen_route) {
                        navController.graph.startDestinationRoute?.let {
                            screen_route -> popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}