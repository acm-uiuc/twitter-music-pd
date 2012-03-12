import daemon

import osc

with daemon.DaemonContext():
	osc.start()

